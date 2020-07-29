const Repository = require('../repositories/Repository');

module.exports.Init = function(app, AllModels) {
    app.get("/categories", function(req, res){

        (async () => {
          const ress = await Repository.categoryRep.GetAllCategories(AllModels.categoryModel);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });
      
      app.post("/createcat", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
          id: 0,
          name: req.body.name,
        }
        Repository.categoryRep.CreateCategory(AllModels.categoryModel, item);
      });

      app.post("/searchcat", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        (async () => {
          const ress = await Repository.categoryRep.SearchCategories(AllModels.categoryModel, req.body.searchText);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/updatecat", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
          id: req.body.id,
          name: req.body.name,
        }
        Repository.categoryRep.UpdateCategory(AllModels.categoryModel, item);
      });
      
      app.post("/deletecat", function(req, res){  
        const id = req.body.id;
        Repository.categoryRep.DeleteCategory(AllModels.categoryModel, id);
      });
}