using Microsoft.EntityFrameworkCore;
using MiApiCrud.Data;
using System.Threading;

var builder = WebApplication.CreateBuilder(args);

// Agregar servicios
builder.Services.AddControllers();
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseSqlServer(
        builder.Configuration.GetConnectionString("DefaultConnection"),
        sqlOptions => sqlOptions.EnableRetryOnFailure()
    ));
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

// Configurar la aplicación
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

// Intentar asegurar/crear la base de datos al iniciar (con reintentos)
try
{
    using var scope = app.Services.CreateScope();
    var db = scope.ServiceProvider.GetRequiredService<AppDbContext>();
    var logger = scope.ServiceProvider.GetRequiredService<ILogger<Program>>();

    const int maxRetries = 10;
    for (int attempt = 1; attempt <= maxRetries; attempt++)
    {
        try
        {
            db.Database.EnsureCreated();
            logger.LogInformation("Database ensured/created successfully.");
            break;
        }
        catch (Exception ex)
        {
            logger.LogWarning(ex, "Attempt {Attempt} - database not ready yet.", attempt);
            if (attempt == maxRetries)
            {
                logger.LogError(ex, "Max attempts reached while ensuring database.");
                throw;
            }
            Thread.Sleep(5000);
        }
    }
}
catch (Exception ex)
{
    // Si falla aquí, dejamos que la app continúe (los endpoints fallarán si DB es necesaria),
    // pero registramos el error.
    var logger = app.Services.GetRequiredService<ILogger<Program>>();
    logger.LogError(ex, "Error while ensuring/creating the database on startup.");
}

app.UseAuthorization();

app.MapControllers();

app.Run();
