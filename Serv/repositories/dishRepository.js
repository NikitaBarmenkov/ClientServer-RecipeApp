const RecipestepsRepository = require('./recipeStepsRepository');
const DishIngredientsRepository = require('./dishIngredientsRepository');
const ReviewRepository = require('./reviewRepository');
const UserRepository = require('./userRepository');
const FavRepository = require('./favouritesRepository');
const IngredientRepository = require('./ingredientRepository');
const Sequelize = require('sequelize');
const Op = Sequelize.Op;
const { QueryTypes } = require('sequelize');

async function GetAllDishes(Model) {
    try {
        return await Model.findAll();
    }
    catch (err) {
        console.log(err);
    }
}

async function SearchDishes(Models, item, sequelize) {
    try {
        if (item.ingredients.length > 0) {
            for (i = 0; i < item.ingredients.length; i++) {
                if (!item.ingredients[i].isAdded){
                    item.ingredients.splice(i, 1);
                }
            }
        }
        if (item.text.length > 0 && item.ingredients.length > 0)//поиск по названию и ингредиентам
        {
            let dishes = [];
            for (j = 0; j < item.ingredients.length; j++) {
                const res = await sequelize.query(
                    "SELECT d.id, " + 
                    "d.name, " + 
                    "d.rating, " + 
                    "d.creation_date, " +
                    "d.update_date, " +  
                    "d.cooktime, " +  
                    "d.is_public, " +  
                    "d.kkal, " + 
                    "d.\"CategoryId\", " + 
                    "d.\"UserId\" " +
                    "FROM public.\"Dishes\" d, public.\"DishIngredients\" i " + 
                    "WHERE d.id=i.\"DishId\" AND i.\"IngredientId\"=" + item.ingredients[j].id + 
                    " AND d.name LIKE '%" + item.text + "%'" + 
                    " AND d.is_public='true'",
                    { type: QueryTypes.SELECT });
                    dishes = dishes.concat(res);
            }
            return dishes;
        }
        else if (item.text.length == 0 && item.ingredients.length > 0) {
            let dishes = [];
            for (j = 0; j < item.ingredients.length; j++) {
                const res = await sequelize.query(
                    "SELECT d.id, " + 
                    "d.name, " + 
                    "d.rating, " + 
                    "d.creation_date, " +
                    "d.update_date, " + 
                    "d.is_public, " + 
                    "d.cooktime, " +
                    "d.is_public, " + 
                    "d.kkal, " + 
                    "d.\"CategoryId\", " + 
                    "d.\"UserId\" " +
                    "FROM public.\"Dishes\" d, public.\"DishIngredients\" i " + 
                    "WHERE d.id=i.\"DishId\" AND i.\"IngredientId\"=" + item.ingredients[j].id + 
                    " AND d.is_public='true'",
                    { type: QueryTypes.SELECT });
                    dishes = dishes.concat(res);
            }
            return dishes;
        }
        else if (item.text.length > 0 && item.ingredients.length == 0) {
            return await Models.dishModel.findAll({where:{name: { [Op.substring]: item.text }, is_public: true}});
        }
        else {
            return [];
        }
    }
    catch (err) {
        console.log(err);
    }
}

async function GetDish(Models, id) {
    try {
        //let item = await Models.dishModel.findByPk(id);
        const item = await Models.dishModel.findAll({where:{id: id}});
        if(!item) return;
        else {
            /*item[0].userName = await UserRepository.GetUserName(Models.userModel, item[0].UserId);
            item[0].ingredients = await DishIngredientsRepository.GetAllDishIngredientsByDishId(Models.dishIngModel, id);
            item[0].recipes = await RecipestepsRepository.GetAllRecipeStepsByDishId(Models.recipeStepModel, id);
            item[0].reviews = await ReviewRepository.GetReviewsByDishId(Models, id);*/
            return item;
        }
    }
    catch (err) {
        console.log(err);
    }
}

async function ChangeRating(Model, item) {
    try {
        await Model.update({
            rating: item.rating,
          }, 
          { where: {id: item.DishId}});
    }
    catch (err) {
        console.log(err);
    }
}

async function Get10DishesByCategoryId(Model, catId) {
    try {
        return await Model.findAll({ where: { CategoryId: catId, is_public: true }, limit: 4, 
            order: [['update_date', 'DESC']] });
    }
    catch (err) {
        console.log(err);
    }
}

async function GetDishesByCategoryId(Model, catId) {
    try {
        return await Model.findAll({where:{CategoryId: catId, is_public: true}, 
            order: [['update_date', 'DESC']]});
    }
    catch (err) {
        console.log(err);
    }
}

async function GetUserDishes(Model, userId, sequelize) {
    try {
        return await Model.findAll({where:{UserId: userId},
        order: [['update_date', 'DESC']] });
    }
    catch (err) {
        console.log(err);
    }
}

async function CreateDish(Models, item) {
    try {
        const res = await Models.dishModel.create({
            name: item.name,
            kkal: item.kkal,
            UserId: item.UserId,
            cooktime: item.cooktime,
            rating: 0.0,
            creation_date: Date.now(),
            update_date: Date.now(),
            is_public: item.is_public,
            CategoryId: item.CategoryId,
          });
        for (i=0; i<item.recipes.length; i++) {
            item.recipes[i].DishId = res.id;
            (async()=>{
                await RecipestepsRepository.CreateRecipeStep(Models.recipeStepModel, item.recipes[i]);
            })();
        }
        for (i=0; i<item.ingredients.length; i++) {
            item.ingredients[i].DishId = res.id;
            //item.ingredients[i] = await HandleIngredientChanges(Models, item.ingredients[i]);
            await DishIngredientsRepository.CreateDishIngredient(Models.dishIngModel, item.ingredients[i]);
        }
        const dish = await GetDish(Models, res.id);
        return dish;
    }
    catch (err) {
        console.log(err);
    }
}

async function HandleIngredientChanges(Models, item) {
    const ingredients = await IngredientRepository.GetAllIngredients(Models.ingredientModel);
    let checkState = {
        isExist: false,
        ingredient: {},
    };
    for (j = 0; j < ingredients.length; j++) {
        if (ingredients[j].name.toLowerCase() == item.ingredientName.toLowerCase()) {
            checkState.isExist = true;//ингредиент с таким именем существует
            checkState.ingredient = ingredients[j];
            break;
        }
    }
    if (!checkState.isExist) {//добавить новый ингредиент
        new_ingredient = {
            name:  item.ingredientName.charAt(0).toUpperCase() + item.ingredientName.slice(1),
        };
        const ress = IngredientRepository.CreateIngredient(Models.ingredientModel, new_ingredient);
        item.IngredientId = ress.id;
    }
    else {
        item.IngredientId = checkState.ingredient.id;
    }
    return item;
}

async function UpdateDish(Models, item) {
    try {
        await Models.dishModel.update({
            name: item.name,
            kkal: item.kkal,
            UserId: item.UserId,
            cooktime: item.cooktime,
            creation_date: item.creation_date,
            update_date: Date.now(),
            is_public: item.is_public,
            CategoryId: item.CategoryId,
          }, { where: {id: item.id}} );
          if (!item.is_public) {
              await HandleFavourites(Models, item.id);
          }
        for (i=0; i<item.recipes.length; i++) {
            if (item.recipes[i].action == 1) {
                item.recipes[i].DishId = item.id;
                (async()=>{
                    await RecipestepsRepository.CreateRecipeStep(Models.recipeStepModel, item.recipes[i]);
                })();
            }
            else if (item.recipes[i].action == 2) {
                (async()=>{
                    await RecipestepsRepository.UpdateRecipeStep(Models.recipeStepModel, item.recipes[i]);
                })();                                     
            }
        }
        if (item.recipestodelete.length > 0) {
            for (i=0; i<item.recipestodelete.length; i++) {
                await RecipestepsRepository.DeleteRecipeStep(Models.recipeStepModel, item.recipestodelete[i])
            }
        }

        for (i=0; i<item.ingredients.length; i++) {
            if (item.ingredients[i].action == 1) {
                item.ingredients[i].DishId = item.id;
                (async()=>{
                    //item.ingredients[i] = HandleIngredientChanges(Models, item.ingredients[i]);
                    await DishIngredientsRepository.CreateDishIngredient(Models.dishIngModel, item.ingredients[i]);
                })();
            }
            else if (item.ingredients[i].action == 2) {
                (async()=>{
                    //item.ingredients[i] = HandleIngredientChanges(Models, item.ingredients[i]);
                    await DishIngredientsRepository.UpdateDishIngredient(Models.dishIngModel, item.ingredients[i]);
                })();                    
            }
        }
        if (item.ingredientstodelete.length > 0) {
            for (i=0; i<item.ingredientstodelete.length; i++) {
                await DishIngredientsRepository.DeleteDishIngredient(Models.dishIngModel, item.ingredientstodelete[i])
            }
        }
    }
    catch (err) {
        console.log(err);
    }
}

async function HandleFavourites(Models, DishId) {
    try {
        FavRepository.DeleteFavouritesByDishId(Models.favModel, DishId);
    }
    catch (err) {
        console.log(err);
    }
}

async function DeleteDish(Models, id) {
    try {
        await Models.dishModel.destroy({
            where: {
              id: id,
            }
          }); 
          HandleFavourites(Models, id);
    }
    catch (err) {
        console.log(err);
    }
}

async function GetFavDishes(Model, item) {
    try {
        const res =  await Model.findAll({where:{id: { [Op.in]: item }}, 
            order: [['update_date', 'DESC']]});
        return res;
    }
    catch (err) {
        console.log(err);
    }
}

module.exports.GetAllDishes = GetAllDishes;
module.exports.GetDish = GetDish;
module.exports.GetFavDishes = GetFavDishes;
module.exports.Get10DishesByCategoryId = Get10DishesByCategoryId;
module.exports.GetDishesByCategoryId = GetDishesByCategoryId;
module.exports.GetUserDishes = GetUserDishes;
module.exports.CreateDish = CreateDish;
module.exports.UpdateDish = UpdateDish;
module.exports.DeleteDish = DeleteDish;
module.exports.SearchDishes = SearchDishes;
module.exports.ChangeRating = ChangeRating;