FROM node:20-alpine
WORKDIR /app

# Instalar dependencias
COPY package*.json ./
RUN npm ci

# Copiar código y construir
COPY . .
ARG VITE_API_BASE=http://gahona-ticket-container:9090
ENV VITE_API_BASE=${VITE_API_BASE}
RUN npm run build

# Exponer puerto de preview
EXPOSE 3001

# Servir el build con vite preview
CMD ["npm", "run", "preview", "--", "--host", "0.0.0.0", "--port", "3001"]