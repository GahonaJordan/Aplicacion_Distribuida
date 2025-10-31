const REST_API = "http://localhost:9091/api/articulos";
  const SOAP_URL = "http://localhost:9091/ws";
  const SOAP_USER = 'admin';
  const SOAP_PASS = 'admin123';

  let articuloEditando = null;

  // Función auxiliar para mostrar/ocultar el indicador de carga
  function toggleLoading(show) {
    document.getElementById('loading').style.display = show ? 'block' : 'none';
  }

  // Carga todos los artículos (REST) - Usando tu endpoint @GetMapping
  async function loadArticulosREST() {
    toggleLoading(true);
    try {
      const res = await fetch(REST_API);
      if (!res.ok) {
        const txt = await res.text();
        throw new Error(`REST error ${res.status}: ${txt.slice(0,200)}`);
      }
      const data = await res.json();
      renderTabla(data);
      showMessage(`Se cargaron ${data.length} artículos mediante REST.`, "success");
    } catch (err) {
      showMessage("Error al cargar artículos: " + err.message, "error");
      console.error(err);
    } finally {
      toggleLoading(false);
    }
  }

  // Buscar artículo individual por SOAP
  async function buscarArticuloSOAP() {
    const codigo = document.getElementById("codigoBuscar").value.trim();
    if (!codigo) {
      showMessage("Ingrese un código para buscar.", "error");
      return;
    }

    const soapEnvelope = `
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                        xmlns:art="http://www.example.com/soap/articulos">
        <soapenv:Header/>
        <soapenv:Body>
          <art:ConsultarArticuloRequest>
            <art:codigo>${codigo}</art:codigo>
          </art:ConsultarArticuloRequest>
        </soapenv:Body>
      </soapenv:Envelope>
    `;

    try {
      toggleLoading(true);
      const res = await fetch(SOAP_URL, {
        method: "POST",
        headers: {
          "Content-Type": "text/xml",
          'Authorization': 'Basic ' + btoa(SOAP_USER + ':' + SOAP_PASS)
        },
        body: soapEnvelope
      });

      if (!res.ok) {
        const t = await res.text();
        throw new Error(`SOAP error ${res.status}: ${t.slice(0,200)}`);
      }

      const text = await res.text();
      const parser = new DOMParser();
      const xml = parser.parseFromString(text, "text/xml");

      const getText = (doc, localName) => {
        const byNS = doc.getElementsByTagNameNS('*', localName);
        if (byNS && byNS.length > 0) return byNS[0].textContent;
        const all = doc.getElementsByTagName('*');
        for (let i = 0; i < all.length; i++) {
          if ((all[i].localName || all[i].nodeName) === localName) return all[i].textContent;
        }
        return null;
      };

      const codigoResp = getText(xml, 'codigo');
      if (!codigoResp) {
        showMessage(`No existe el artículo con código ${codigo}.`, "error");
        return;
      }

      const art = {
        codigo: codigoResp,
        nombre: getText(xml, 'nombre'),
        categoria: getText(xml, 'categoria'),
        precioCompra: getText(xml, 'precioCompra'),
        precioVenta: getText(xml, 'precioVenta'),
        stock: getText(xml, 'stock'),
        stockMinimo: getText(xml, 'stockMinimo'),
        proveedor: getText(xml, 'proveedor')
      };
      renderTabla([art]);
      showMessage(`Artículo ${codigo} encontrado mediante SOAP.`, "success");
    } catch (err) {
      showMessage("Error al buscar artículo: " + err.message, "error");
      console.error(err);
    } finally {
      toggleLoading(false);
    }
  }

  // Crear nuevo artículo via SOAP (similar a app.js)
  function escapeXml(unsafe) {
    if (unsafe === undefined || unsafe === null) return '';
    return String(unsafe)
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&apos;');
  }

  async function crearArticuloSOAP(articulo) {
    const soap = `
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                        xmlns:art="http://www.example.com/soap/articulos">
        <soapenv:Header/>
        <soapenv:Body>
          <art:InsertarArticuloRequest>
            <art:articulo>
              <art:codigo>${escapeXml(articulo.codigo)}</art:codigo>
              <art:nombre>${escapeXml(articulo.nombre)}</art:nombre>
              <art:categoria>${escapeXml(articulo.categoria)}</art:categoria>
              <art:precioCompra>${escapeXml(articulo.precioCompra)}</art:precioCompra>
              <art:precioVenta>${escapeXml(articulo.precioVenta)}</art:precioVenta>
              <art:stock>${escapeXml(articulo.stock)}</art:stock>
              <art:stockMinimo>${escapeXml(articulo.stockMinimo)}</art:stockMinimo>
              <art:proveedor>${escapeXml(articulo.proveedor)}</art:proveedor>
            </art:articulo>
          </art:InsertarArticuloRequest>
        </soapenv:Body>
      </soapenv:Envelope>`;

    try {
      toggleLoading(true);
      const res = await fetch(SOAP_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'text/xml',
          'Authorization': 'Basic ' + btoa(SOAP_USER + ':' + SOAP_PASS)
        },
        body: soap
      });

      const text = await res.text();
      if (!res.ok) {
        throw new Error(`SOAP error ${res.status}: ${text.slice(0,200)}`);
      }

      // parse response and detect faults
      const doc = new DOMParser().parseFromString(text, 'application/xml');
      const fault = doc.getElementsByTagNameNS('*', 'Fault')[0] || doc.getElementsByTagName('Fault')[0];
      if (fault) {
        const faultStr = fault.textContent || 'SOAP Fault';
        throw new Error(faultStr);
      }

  showMessage('Artículo creado correctamente (vía SOAP).', 'success');
      // refrescar lista REST para ver el nuevo artículo (si procede)
      try { await loadArticulosREST(); } catch(e) { /* ignore */ }
      return text;
    } catch (err) {
      showMessage('Error al crear artículo (SOAP): ' + err.message, 'error');
      console.error(err);
      throw err;
    } finally {
      toggleLoading(false);
    }
  }

  // Actualizar artículo (REST) - Usando tu endpoint @PutMapping
  async function actualizarArticulo(codigo, articulo) {
    try {
      const res = await fetch(`${REST_API}/${codigo}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(articulo)
      });

      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(errorText || 'Error al actualizar artículo');
      }

      const data = await res.json();
      showMessage("Artículo actualizado correctamente.", "success");
      loadArticulosREST();
      return data;
    } catch (err) {
      showMessage("Error al actualizar artículo: " + err.message, "error");
      throw err;
    }
  }

  // Eliminar artículo (REST) - Usando tu endpoint @DeleteMapping
  async function eliminarArticulo(codigo) {
    if (!confirm(`¿Seguro que desea eliminar el artículo ${codigo}?`)) return;

    try {
      const res = await fetch(`${REST_API}/${codigo}`, { 
        method: "DELETE" 
      });

      if (res.status === 204) {
        showMessage("Artículo eliminado correctamente.", "success");
        loadArticulosREST();
      } else if (res.status === 404) {
        showMessage("Artículo no encontrado.", "error");
      } else {
        const errorText = await res.text();
        throw new Error(errorText || 'Error al eliminar artículo');
      }
    } catch (err) {
      showMessage("Error al eliminar: " + err.message, "error");
    }
  }

  // Funciones del formulario
  function mostrarFormularioCrear() {
    articuloEditando = null;
    document.getElementById('tituloFormulario').textContent = 'Crear Nuevo Artículo';
    document.getElementById('articuloForm').reset();
    document.getElementById('formularioArticulo').style.display = 'block';
  }

  function mostrarFormularioEditar(articulo) {
    articuloEditando = articulo;
    document.getElementById('tituloFormulario').textContent = 'Editar Artículo';
    document.getElementById('codigo').value = articulo.codigo || '';
    document.getElementById('nombre').value = articulo.nombre || '';
    document.getElementById('categoria').value = articulo.categoria || '';
    document.getElementById('proveedor').value = articulo.proveedor || '';
    document.getElementById('precioCompra').value = articulo.precioCompra || '';
    document.getElementById('precioVenta').value = articulo.precioVenta || '';
    document.getElementById('stock').value = articulo.stock || 0;
    document.getElementById('stockMinimo').value = articulo.stockMinimo || 0;
    document.getElementById('formularioArticulo').style.display = 'block';
  }

  function ocultarFormulario() {
    document.getElementById('formularioArticulo').style.display = 'none';
    articuloEditando = null;
  }

  async function guardarArticulo(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const articulo = {
      codigo: formData.get('codigo'),
      nombre: formData.get('nombre'),
      categoria: formData.get('categoria'),
      proveedor: formData.get('proveedor'),
      precioCompra: parseFloat(formData.get('precioCompra')),
      precioVenta: parseFloat(formData.get('precioVenta')),
      stock: parseInt(formData.get('stock')),
      stockMinimo: parseInt(formData.get('stockMinimo'))
    };

    try {
      if (articuloEditando) {
        await actualizarArticulo(articuloEditando.codigo, articulo);
      } else {
        await crearArticuloSOAP(articulo);
      }
      ocultarFormulario();
    } catch (err) {
      console.error('Error al guardar artículo:', err);
    }
  }

  // Función que combina ambos enfoques según necesidad
  async function cargarTodo() {
    await loadArticulosREST();
  }

  // Renderizar tabla
  function renderTabla(articulos) {
  const tbody = document.querySelector("#tablaArticulos tbody");
  tbody.innerHTML = "";

  if (articulos.length === 0) {
    const tr = document.createElement("tr");
    tr.innerHTML = `<td colspan="9" style="text-align: center;">No se encontraron artículos</td>`;
    tbody.appendChild(tr);
    return;
  }

  let hayStockBajo = false;

  articulos.forEach(a => {
    const stockVal = parseInt(a.stock || '0');
    const minimoVal = parseInt(a.stockMinimo || '0');
    const lowStock = !isNaN(stockVal) && !isNaN(minimoVal) && stockVal < minimoVal;
    if (lowStock) hayStockBajo = true;

    const tr = document.createElement("tr");
    const safeCodigo = String(a.codigo || '').replace(/'/g, "\\'");

    tr.innerHTML = `
      <td>${a.codigo || ''}</td>
      <td>${a.nombre || ''}</td>
      <td>${a.categoria || ''}</td>
      <td>${a.precioCompra || ''}</td>
      <td>${a.precioVenta || ''}</td>
      <td class="${lowStock ? 'low-stock' : 'ok-stock'}">${a.stock || ''}</td>
      <td>${a.stockMinimo || ''}</td>
      <td>${a.proveedor || ''}</td>
      <td>
        <button class="btn btn-update" onclick="mostrarFormularioEditar(${JSON.stringify(a).replace(/"/g, '&quot;')})">Editar</button>
        <button class="btn btn-delete" onclick="eliminarArticulo('${safeCodigo}')">Eliminar</button>
      </td>
    `;
    tbody.appendChild(tr);
  });

  // Mostrar mensaje si hay stock bajo
  const msgDiv = document.getElementById("message");
  if (hayStockBajo) {
    msgDiv.innerHTML = `<div class="message warning">Atención: Hay artículos con stock por debajo del mínimo.</div>`;
  } else {
    msgDiv.innerHTML = "";
  }
}


  // Mostrar mensaje visual
  function showMessage(msg, type) {
    const div = document.getElementById("message");
    div.innerHTML = `<div class="message ${type}">${msg}</div>`;
    setTimeout(() => {
      div.innerHTML = '';
    }, 5000);
  }

  // Cargar al inicio usando REST
  cargarTodo();