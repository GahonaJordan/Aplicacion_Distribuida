using Microsoft.EntityFrameworkCore;
using MiApiCrud.Models;

namespace MiApiCrud.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        public DbSet<Producto> Productos { get; set; }
    }
}
