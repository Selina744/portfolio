using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using SportsStore.Domain.Abstract;
using SportsStore.Domain.Entities;
using SportsStore.WebUI.Infrastructure;
using SportsStore.WebUI.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace SportsStore.WebUI.Controllers
{
    public class CartController : Controller
    {
        private IProductRepository repository;
        private ICartRepository cartRepository;

        //repos bound thru ninject? Does this create a new one each time or does it use the same one?
        //How is this different from the cart binding in ModelBinders in Application_Start?
        public CartController(IProductRepository repo, ICartRepository cartRepo)
        {
            repository = repo;
            cartRepository = cartRepo;
        }
        public ViewResult Index(Cart cart, string returnUrl)
        {
            return View(new CartIndexViewModel
            {
                Cart = GetCart(),
                ReturnUrl = returnUrl
            });
        }

        public RedirectToRouteResult AddToCart(int productId, string returnUrl)
        {
            Product product = repository.Products.FirstOrDefault(p => p.ProductID == productId);
            if (product != null)
            {
                if(HttpContext.GetOwinContext().GetUserManager<SportsStoreUserManager>().FindByIdAsync(HttpContext.User.Identity.GetUserId()).Result == null)
                {
                    GetCart().AddItem(product, 1);
                }
                else
                {
                    int quantity = 0;
                    string userID = HttpContext.User.Identity.GetUserId();
                    CartLine cartLine = cartRepository.CartLines.Where(x => x.UserID == userID && x.ProductID == productId).FirstOrDefault();
                    if (cartLine != null)
                    {
                        quantity = cartLine.Quantity;
                    }
                    cartRepository.AddItem(product.ProductID, quantity, userID);
                }

            }
            return RedirectToAction("Index", new { returnUrl });
        }

        public RedirectToRouteResult RemoveFromCart(int productId, string returnUrl)
        {
            Product product = repository.Products.FirstOrDefault(p => p.ProductID == productId);
            if (product != null)
            {
                GetCart().RemoveLine(product);
            }
            return RedirectToAction("Index", new { returnUrl });
        }
        public PartialViewResult Summary()
        {
            return PartialView(GetCart());
        }
        public ViewResult Checkout()
        {
            return View(new ShippingDetails());
        }

        private Cart GetCart()
        {
            User user = HttpContext.GetOwinContext().GetUserManager<SportsStoreUserManager>().FindByIdAsync(HttpContext.User.Identity.GetUserId()).Result;
            Cart cart = null;
            if(user == null)
            {
                cart = (Cart)Session["Cart"];
                if (cart == null)
                {
                    cart = new Cart();
                    Session["Cart"] = cart;
                }
            }
            else
            {
                IEnumerable<CartLine> cartLines = cartRepository.CartLines.Where(x => x.UserID == user.Id);
                cart = new Cart();
                foreach (var item in cartLines)
                {
                    cart.AddLine(new CartLineM { Product = repository.GetProduct(item.ProductID), Quantity = item.Quantity });
                }
            }

            return cart;
        }
    }

}