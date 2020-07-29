const Repository = require('../repositories/Repository');

module.exports.Init = function(app, AllModels) {
      app.post("/createreview", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
          id: 0,
          UserId: req.body.UserId,
          DishId: req.body.DishId,
          rating: req.body.rating,
          comment: req.body.comment,
        };
        (async () => {
          const ress = await Repository.reviewRep.CreateReview(AllModels, item);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/dishreviews", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        (async () => {
          const ress = await Repository.reviewRep.GetReviewsByDishId(AllModels, req.body.id);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/userdishreview", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
            DishId: req.body.DishId,
            UserId: req.body.UserId,
        };
        (async () => {
          const ress = await Repository.reviewRep.GetReviewByUserDishId(AllModels.reviewModel, item);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/updatereview", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
            id: req.body.id,
            UserId: req.body.UserId,
            DishId: req.body.DishId,
            rating: req.body.rating,
            comment: req.body.comment,
          };
          (async () => {
            await Repository.reviewRep.UpdateReview(AllModels, item);
            const ress = await Repository.reviewRep.GetReview(AllModels, item.id);
            let data = JSON.stringify(ress);
            res.json(data);
          })();
      });
      
      app.post("/deletereview", function(req, res){
        if(!req.body) return res.sendStatus(400);
        (async () => {
          await Repository.reviewRep.DeleteReview(AllModels, req.body.id);
          res.sendStatus(200);
        })();
      });
}