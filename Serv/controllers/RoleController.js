const Repository = require('../repositories/Repository');

module.exports.Init = function(app, AllModels) {
    app.get("/roles", function(req, res){
        (async () => {
          const ress = await Repository.roleRep.GetAllRoles(AllModels.roleModel);
          let data = JSON.stringify(ress);
          res.json(data);
        })();
      });
}