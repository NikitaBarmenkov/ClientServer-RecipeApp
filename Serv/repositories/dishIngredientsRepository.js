async function GetAllDishIngredientsByDishId(Model, dishId) {
    try {
        return await Model.findAll({where:{DishId: dishId}});
    }
    catch (err) {
        console.log(err);
    }
}

async function GetAllDishIngredientsByIngredientId(Model, ingredientId) {
    try {
        return await Model.findAll({where:{IngredientId: ingredientId}});
    }
    catch (err) {
        console.log(err);
    }
}

async function CreateDishIngredient(Model, item) {
    try {
        return await Model.create({
            quantity: item.quantity,
            unit: item.unit,
            IngredientId: item.IngredientId,
            DishId: item.DishId,
          });
    }
    catch (err) {
        console.log(err);
    }
}

async function UpdateDishIngredient(Model, item) {
    try {
        await Model.update({ 
            quantity: item.quantity,
            unit: item.unit,
            IngredientId: item.IngredientId,
            DishId: item.DishId,
        }, {
            where: {
              id: item.id,
            }
          });
    }
    catch (err) {
        console.log(err);
    }
}

async function DeleteDishIngredient(Model, id) {
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

module.exports.GetAllDishIngredientsByDishId = GetAllDishIngredientsByDishId;
module.exports.GetAllDishIngredientsByIngredientId = GetAllDishIngredientsByIngredientId;
module.exports.CreateDishIngredient = CreateDishIngredient;
module.exports.UpdateDishIngredient = UpdateDishIngredient;
module.exports.DeleteDishIngredient = DeleteDishIngredient;