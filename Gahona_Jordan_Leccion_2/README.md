================================================= Requisitos del entorno ========================================
- Ejecutar el docker compone para crear la base de datos de mysql.
- luego ejecutar los comandos de la "Construcción de imagen Docker" para poner los contenedores en la misma red.
- luego ejecutar los comandos para "Comando para ejecutar contenedor" ya esta puesta en la red creada y el puerto deseado.
- en postman ejecutar cada uno de los links para pruebas, el primer link es el unico post, los demas son get

==================================================== Construcción de imagen Docker ===========================================

//limpiamos paquetes
.\mvnw.cmd clean package -DskipTests

//entramos en el target pero en este caso no es necesario
cd .\target\

//creamos la red
docker network create ticket-net

//asociamos la base con la red
docker network connect ticket-net mysql_tickets

//verificamos
docker inspect mysql_tickets

//construimos la imagen del back
docker build -t gahona-ticket:latest .

================================================== Comando para ejecutar contenedor ======================================0

//ejecutamos el contenedor
docker run --name gahona-ticket-container --network ticket-net -p 9090:9090 gahona-ticket:latest

======================================== URL base y ejemplos de consumo ============================================

crear: http://localhost:9090/api/v1/support-tickets
body del crear:
{
    "ticketNumber": "ST-2025-000001",
    "requesterName": "Juan Pérez",
    "status": "OPEN",
    "priority": "HIGH",
    "category": "NETWORK",
    "estimatedCost": 150.50,
    "currency": "USD",
    "dueDate": "2025-12-31"
}
buscar por nombre: http://localhost:9090/api/v1/support-tickets?q=juan
buscar po moneda: http://localhost:9090/api/v1/support-tickets?status=OPEN&currency=USD
buscar por fechas: http://localhost:9090/api/v1/support-tickets?from=2025-01-01T00:00:00&to=2025-12-31T23:59:59
buscar todo: http://localhost:9090/api/v1/support-tickets
buscar por estatus: http://localhost:9090/api/v1/support-tickets?status=OPEN
buscar por minimo costo: http://localhost:9090/api/v1/support-tickets?minCost=50
buscar por maximo costo: http://localhost:9090/api/v1/support-tickets?maxCost=300
