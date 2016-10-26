using SportsStore.Domain.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportsStore.Domain.Abstract
{
    public interface ICartRepository
    {
        IEnumerable<CartLine> CartLines { get; }
        void AddItem(int productid, int quantity, string userID);
        void Save();
    }
}
