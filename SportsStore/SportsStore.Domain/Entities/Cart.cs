using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportsStore.Domain.Entities
{
    public class Cart
    {
        private List<CartLineM> lineCollection = new List<CartLineM>();

        public void AddItem(Product product, int quantity)
        {
            CartLineM line = lineCollection.Where(p => p.Product.ProductID == product.ProductID).FirstOrDefault();
            if(line == null)
            {
                lineCollection.Add(new CartLineM { Product = product, Quantity = quantity });
            }
            else
            {
                line.Quantity += quantity;
            }
        }

        public void AddLine(CartLineM line)
        {
            lineCollection.Add(line);
        }

        public void RemoveLine(Product product)
        {
            lineCollection.RemoveAll(l => l.Product.ProductID == product.ProductID);
        }

        public decimal ComputeTotalValue()
        {
            return lineCollection.Sum(e => e.Product.Price * e.Quantity);
        }
        public void Clear()
        {
            lineCollection.Clear();
        }

        public IEnumerable<CartLineM> Lines
        {
            get { return lineCollection; }
        }
    }

    public class CartLineM
    {
        public Product Product { get; set; }
        public int Quantity { get; set; }
    }

    public class CartLine
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int CartLineID { get; set; }
        public string UserID { get; set; }
        public int ProductID { get; set; }
        public int Quantity { get; set; }
    }
}
