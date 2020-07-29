const Sequelize = require('sequelize');
const Op = Sequelize.Op;

async function GetAllIngredients(Model) {
    try {
        return await Model.findAll();
    }
    catch (err) {
        console.log(err);
    }
}

async function SearchIngs(Model, text) {
    try {
        return await Model.findAll({where:{name: { [Op.substring]: text }}});
    }
    catch (err) {
        console.log(err);
    }
}

async function CreateIngredient(Model, item) {
    try {
        return await Model.create({
            name: item.name
          });
    }
    catch (err) {
        console.log(err);
    }
}

function UpdateIngredient(Model, item) {
    Model.update({ name: item.name }, {
        where: {
          id: item.id,
        }
      }).then((res) => {
        console.log(res);
      });
}

async function DeleteIngredient(Model, id) {
    try {
        return await Model.destroy({
            where: {
              id: id,
            }
          }); 
    }
    catch (err) {
        console.log(err);
    }
}

module.exports.GetAllIngredients = GetAllIngredients;
module.exports.CreateIngredient = CreateIngredient;
module.exports.UpdateIngredient = UpdateIngredient;
module.exports.DeleteIngredient = DeleteIngredient;
module.exports.SearchIngs = SearchIngs;