const Repository = require('../repositories/Repository');

module.exports.Init = function(app, AllModels) {
      app.post("/addtobuylist", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
          id: 0,
          UserId: req.body.UserId,
          items: req.body.items,
        };
        (async()=>{
          await Repository.buyIngsRep.CreateBuyIng(AllModels.buyIngModel, item);
          res.sendStatus(200);
        })();
      });

      app.post("/userbuylist", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        (async () => {
          const ress = await Repository.buyIngsRep.GetAllUserBuyIngs(AllModels.buyIngModel, req.body.id);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/updatebuylistitem", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
            id: req.body.id,
            name: req.body.name,
            quantity: req.body.quantity,
            UserId: req.body.UserId,
            checked: req.body.checked,
        };
        (async () => {
          const ress = await Repository.buyIngsRep.UpdateBuyIng(AllModels.buyIngModel, item);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });
      
      app.post("/deletebuylist", function(req, res){  
        if(!req.body) return res.sendStatus(400);
        (async()=>{
          const ress = await Repository.buyIngsRep.DeleteBuyIng(AllModels.buyIngModel, req.body.id);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });
}