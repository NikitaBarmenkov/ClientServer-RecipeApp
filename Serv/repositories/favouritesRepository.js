const Sequelize = require('sequelize');

async function GetAllUserFavourites(Model, id) {
    try {
        return await Model.findAll({where: {UserId: id}}); 
    }
    catch (err) {
        console.log(err);
    }
}

async function DeleteFavouritesByDishId(Model, dishId) {
    try {
        await Model.destroy({
            where: {
              DishId: dishId,
            }
          }); 
    }
    catch (err) {
        console.log(err);
    }
}

async function GetUserFavouriteForDish(Model, item) {
    try {
        return await Model.findAll({where: {UserId: item.UserId, DishId: item.DishId}});
    }
    catch (err) {
        console.log(err);
    }
}

async function CreateFavourite(Model, item) {
    try {
        return await Model.create({
            UserId: item.UserId,
            DishId: item.DishId,
          });
    }
    catch (err) {
        console.log(err);
    }
}

async function DeleteFavourite(Model, id) {
    try {
        await Model.destroy({
            where: {
              id: id,
            }
          }); 
    }
    catch (err) {
        console.log(err);
    }
}

module.exports.GetAllUserFavourites = GetAllUserFavourites;
module.exports.CreateFavourite = CreateFavourite;
module.exports.GetUserFavouriteForDish = GetUserFavouriteForDish;
module.exports.DeleteFavourite = DeleteFavourite;
module.exports.DeleteFavouritesByDishId = DeleteFavouritesByDishId;