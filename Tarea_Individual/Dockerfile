# Etapa 1: Build
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src

# Copiar el archivo de proyecto y restaurar dependencias
COPY MiApiCrud/*.csproj MiApiCrud/
RUN dotnet restore MiApiCrud/MiApiCrud.csproj

# Copiar el resto del código y compilar
COPY . .
WORKDIR /src/MiApiCrud
RUN dotnet publish -c Release -o /app/out

# Etapa 2: Runtime
FROM mcr.microsoft.com/dotnet/aspnet:8.0
WORKDIR /app
COPY --from=build /app/out .
EXPOSE 8080
ENTRYPOINT ["dotnet", "MiApiCrud.dll"]
