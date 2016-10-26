using SportsStore.Domain.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace SportsStore.WebUI.Infrastructure.Binders
{
    public class CartModelBinder : IModelBinder
    {
        private const string sessionKey = "Cart";
        public object BindModel(ControllerContext controllerContext, ModelBindingContext bindingContext)
        {
            Cart cart = null;
            //User user = controllerContext.HttpContext.GetOwinContext().GetUserManager<SportsStoreUserManager>().FindByIdAsync(controllerContext.HttpContext.User.Identity.GetUserId()).Result;
            //if(user == null)
            //{
            if (controllerContext.HttpContext.Session != null)
            {
                cart = (Cart)controllerContext.HttpContext.Session[sessionKey];
            }
            if (cart == null)
            {
                cart = new Cart();
                if (controllerContext.HttpContext.Session != null)
                {
                    controllerContext.HttpContext.Session[sessionKey] = cart;
                }
            }
            //}
            //else
            //{
            //    IEnumerable<CartLine> cartLines = repo.CartLines.Where(x => x.User == user);
            //    cart = new Cart(cartLines.ToList());
            //}
            return cart;
        }
    }
}