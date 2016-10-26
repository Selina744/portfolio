using SportsStore.Domain.Abstract;
using SportsStore.Domain.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportsStore.Domain.Concrete
{
    public class EFCartRepository : ICartRepository
    {
        private EFDbContext context = new EFDbContext();

        public IEnumerable<CartLine> CartLines
        {
            get
            {
                return context.CartLines;
            }
        }

        public void AddItem(int productid, int quantity, string userID)
        {
            CartLine cartLine = new CartLine { ProductID = productid, UserID = userID, Quantity = quantity + 1 };
            context.CartLines.Add(cartLine);
            context.SaveChanges();
        }

        public void Save()
        {
            context.SaveChanges();
        }
    }
}
