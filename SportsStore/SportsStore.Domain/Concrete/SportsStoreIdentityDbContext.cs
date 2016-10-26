using Microsoft.AspNet.Identity.EntityFramework;
using SportsStore.Domain.Entities;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportsStore.Domain.Concrete
{
    public class SportsStoreIdentityDbContext : IdentityDbContext<User>
    {
        public SportsStoreIdentityDbContext() : base("EFDbContext") { }

        static SportsStoreIdentityDbContext()
        {
            Database.SetInitializer<SportsStoreIdentityDbContext>(new IdentityDbInit());
        }
        public static SportsStoreIdentityDbContext Create()
        {
            return new SportsStoreIdentityDbContext();
        }
    }

    public class IdentityDbInit : DropCreateDatabaseIfModelChanges<SportsStoreIdentityDbContext>
    {
        protected override void Seed(SportsStoreIdentityDbContext context)
        {
            PerformInitialSetup(context);
            base.Seed(context);
        }
        public void PerformInitialSetup(SportsStoreIdentityDbContext context)
        {
            //init config here
        }
    }
}
