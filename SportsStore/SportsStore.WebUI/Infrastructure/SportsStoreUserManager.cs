using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.Owin;
using SportsStore.Domain.Entities;
using SportsStore.Domain.Concrete;

namespace SportsStore.WebUI.Infrastructure
{
    public class SportsStoreUserManager : UserManager<User>
    {
        public SportsStoreUserManager(IUserStore<User> store) : base(store) {

        }

        public static SportsStoreUserManager Create(IdentityFactoryOptions<SportsStoreUserManager> options, IOwinContext context)
        {
            SportsStoreIdentityDbContext db = context.Get<SportsStoreIdentityDbContext>();
            SportsStoreUserManager manager = new SportsStoreUserManager(new UserStore<User>(db));

            manager.PasswordValidator = new PasswordValidator
            {
                RequiredLength = 7,
                RequireNonLetterOrDigit = false,
                RequireDigit = false,
                RequireLowercase = true,
                RequireUppercase = false
            };
            manager.UserValidator = new UserValidator<User>(manager)
            {
                AllowOnlyAlphanumericUserNames = true,
                RequireUniqueEmail = true
            };
            return manager;
        }
    }
}