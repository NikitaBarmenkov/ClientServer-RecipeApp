const Repository = require('../repositories/Repository');

module.exports.Init = function(app, AllModels) {
    app.post("/userfavs", function(req, res){
      if(!req.body) return res.sendStatus(400);
        (async () => {
          const ress = await Repository.favouritesRep.GetAllUserFavourites(AllModels.favModel, req.body.id);
          const ress1 = [];
          for (i = 0; i < ress.length; i++) {
            ress1[i] = ress[i].DishId;
          }
          const ress2 = await Repository.dishRep.GetFavDishes(AllModels.dishModel, ress1);
          let data = JSON.stringify(ress2);
          res.json(data);
        })();
      });
      
      app.post("/createfav", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
          id: 0,
          UserId: req.body.UserId,
          DishId: req.body.DishId,
        };
        (async()=> {
          const ress = await Repository.favouritesRep.CreateFavourite(AllModels.favModel, item);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/userfavfordish", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
            UserId: req.body.UserId,
            DishId: req.body.DishId,
        };
        (async() => {
          const ress = await Repository.favouritesRep.GetUserFavouriteForDish(AllModels.favModel, item);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });
      
      app.post("/deletefav", function(req, res){  
        const id = req.body.id;
        Repository.favouritesRep.DeleteFavourite(AllModels.favModel, id);
        res.sendStatus(204);
      });
}