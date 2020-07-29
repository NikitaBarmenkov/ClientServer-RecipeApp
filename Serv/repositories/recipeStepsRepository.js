async function GetAllRecipeStepsByDishId(Model, dishId) {
    try {
        return await Model.findAll({where:{DishId: dishId}});
    }
    catch (err) {
        console.log(err);
    }
}

async function CreateRecipeStep(Model, item) {
    try {
        return await Model.create({
            text: item.text,
            number: item.number,
            DishId: item.DishId,
          });
    }
    catch (err) {
        console.log(err);
    }
}

async function UpdateRecipeStep(Model, item) {
    try {
        await Model.update({ 
            text: item.text,
            number: item.number,
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

async function DeleteRecipeStep(Model, id) {
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

module.exports.GetAllRecipeStepsByDishId = GetAllRecipeStepsByDishId;
module.exports.CreateRecipeStep = CreateRecipeStep;
module.exports.UpdateRecipeStep = UpdateRecipeStep;
module.exports.DeleteRecipeStep = DeleteRecipeStep;