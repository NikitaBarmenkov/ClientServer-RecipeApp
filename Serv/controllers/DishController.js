const Repository = require('../repositories/Repository');

module.exports.Init = function(app, AllModels, sequelize) {
    app.get("/dishes", function(req, res){
        (async () => {
          const ress = await Repository.dishRep.GetAllDishes(AllModels.dishModel);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/categorydishes", function(req, res){
        if(!req.body) return res.sendStatus(400);
        (async () => {
          const ress = await Repository.dishRep.GetDishesByCategoryId(AllModels.dishModel, req.body.id);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/category10dishes", function(req, res){
        if(!req.body) return res.sendStatus(400);
        (async () => {
          const ress = await Repository.dishRep.Get10DishesByCategoryId(AllModels.dishModel, req.body.id);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/userdishes", function(req, res){
        if(!req.body) return res.sendStatus(400);
        (async () => {
          let ress = await Repository.dishRep.GetUserDishes(AllModels.dishModel, req.body.id, sequelize);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/searchdishes", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
          text: req.body.text,
          ingredients: req.body.ingredients,
        };
        (async () => {
          const ress = await Repository.dishRep.SearchDishes(AllModels, item, sequelize);
          for (i = 0; i < ress.length - 1; i++) {
            for (j = i + 1; j < ress.length; j++) {
              if (ress[i].id == ress[j].id) {
                ress.splice(i, 1);
              }
            }
          }
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/dish", function(req, res){
        if(!req.body) return res.sendStatus(400);
        (async () => {
          const ress = await Repository.dishRep.GetDish(AllModels, req.body.id);
          let data = JSON.stringify(ress[0]);
          res.json(data);
        })();
      });

      app.post("/dishrecipes", function(req, res){
        if(!req.body) return res.sendStatus(400);
        (async () => {
          const ress = await Repository.recipeStepRep.GetAllRecipeStepsByDishId(AllModels.recipeStepModel, req.body.id);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/dishingredients", function(req, res){
        if(!req.body) return res.sendStatus(400);
        (async () => {
          const ress = await Repository.dishIngredientRep.GetAllDishIngredientsByDishId(AllModels.dishIngModel, req.body.id);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/dishauthor", function(req, res){
        if(!req.body) return res.sendStatus(400);
        (async () => {
          const ress = await Repository.userRep.GetUserName(AllModels.userModel, req.body.id);
          res.send(ress);
        })();
      });

      app.post("/dishreviews", function(req, res){
        if(!req.body) return res.sendStatus(400);
        (async () => {
          const ress = await Repository.reviewRep.GetReviewsByDishId(AllModels, req.body.id);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });
      
      app.post("/createdish", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
          id: 0,
          name: req.body.name,
          kkal: req.body.kkal,
          CategoryId: req.body.CategoryId,
          UserId: req.body.UserId,
          cooktime: req.body.cooktime,
          recipes: req.body.recipes,
          is_public: req.body.is_public,
          ingredients: req.body.ingredients,
          recipestodelete: req.body.recipeStepsToDelete,
          ingredientstodelete: req.body.dishIngredientsToDelete,
        };
        (async() =>{
          const ress = await Repository.dishRep.CreateDish(AllModels, item);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });
      
      app.post("/updatedish", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
          id: req.body.id,
          name: req.body.name,
          kkal: req.body.kkal,
          CategoryId: req.body.CategoryId,
          UserId: req.body.UserId,
          cooktime: req.body.cooktime,
          recipes: req.body.recipes,
          is_public: req.body.is_public,
          ingredients: req.body.ingredients,
          recipestodelete: req.body.recipeStepsToDelete,
          ingredientstodelete: req.body.dishIngredientsToDelete,
        };
        (async()=>{
          await Repository.dishRep.UpdateDish(AllModels, item);
          //const resss = await Repository.dishRep.GetDish(AllModels.dishModel, item.id);
          res.sendStatus(201);
        })();
      });
      
      app.post("/deletedish", function(req, res){  
        if(!req.body) return res.sendStatus(400);
        (async()=>{
          await Repository.dishRep.DeleteDish(AllModels, req.body.id);
        })();
        res.sendStatus(204);
      });
}