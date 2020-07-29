const Repository = require('../repositories/Repository');

module.exports.Init = function(app, AllModels) {
    app.get("/users", function(req, res){

        (async () => {
          const ress = await Repository.userRep.GetAllUsers(AllModels.userModel);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/searchuser", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        (async () => {
          const ress = await Repository.userRep.SearchUsers(AllModels.userModel, req.body.searchText);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });
      
      app.post("/createuser", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
          id: 0,
          name: req.body.name,
          password: req.body.password,
          date: Date.now(),
          email: req.body.email,
          RoleId: req.body.RoleId,
        };
        (async() => {
          const ress = await Repository.userRep.CreateUser(AllModels.userModel, item);
          const resss = await Repository.userRep.CheckUser(AllModels.userModel, ress);
          let data = JSON.stringify(resss);
          res.json(data);
        })();
        /*Repository.userRep.CreateUser(AllModels.userModel, item);
        Repository.userRep.CheckUser(AllModels.userModel, item).then(ress => {
          let data = JSON.stringify(ress);
          res.json(data);
        });*/
      });

      app.post("/getusername", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        (async()=>{
          const ress = await Repository.userRep.GetUserName(AllModels.userModel, req.body.id);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });

      app.post("/userlogin", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
          name: req.body.name,
          password: req.body.password
        };
        (async()=>{
          const ress = await Repository.userRep.CheckUser(AllModels.userModel, item);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });
      
      app.post("/updateuser", function (req, res) {
        if(!req.body) return res.sendStatus(400);
        item = {
          id: req.body.id,
          name: req.body.name,
          date: req.body.date,
          email: req.body.email,
          password: req.body.password,
          RoleId: req.body.RoleId,
        };
        (async()=>{
          await Repository.userRep.UpdateUser(AllModels.userModel, item);
          const ress = await Repository.userRep.CheckUser(AllModels.userModel, item);
          let data = JSON.stringify(ress[0]);
          res.json(data);
        })();
      });
      
      app.post("/deleteuser", function(req, res){
        const id = req.body.id;
        Repository.userRep.DeleteUser(AllModels.userModel, id);
      });
}