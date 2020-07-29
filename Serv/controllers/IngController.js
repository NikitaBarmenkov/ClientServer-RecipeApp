const Repository = require('../repositories/Repository');

module.exports.Init = function(app, AllModels) {
    app.get("/ingredients", function(req, res){
        (async () => {
          const ress = await Repository.ingredientRep.GetAllIngredients(AllModels.ingredientModel);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/searching", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        (async () => {
          const ress = await Repository.ingredientRep.SearchIngs(AllModels.ingredientModel, req.body.searchText);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });
      
      app.post("/createing", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
          id: 0,
          name: req.body.name,
        }
        Repository.ingredientRep.CreateIngredient(AllModels.ingredientModel, item);
      });
      
      app.post("/updateing", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
          id: req.body.id,
          name: req.body.name,
        }
        Repository.ingredientRep.UpdateIngredient(AllModels.ingredientModel, item);
      });
      
      app.post("/deleteing", function(req, res){  
        const id = req.body.id;
        Repository.ingredientRep.DeleteIngredient(AllModels.ingredientModel, id);
      });
}