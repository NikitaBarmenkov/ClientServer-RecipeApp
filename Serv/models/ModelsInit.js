const dish = require('./dish.js');
const category = require('./category.js');
const ingredient = require('./ingredient.js');
const recipeStep = require('./recipeStep');
const dishIng = require('./dishIngredient');
const user = require('./user');
const role = require('./role');
const review = require('./review');
const buyIng = require('./buyIngredient');
const fav = require('./favourites');

module.exports.GetModels = function(seq) {
    const RecipeStep = recipeStep.RecipeStepInit(seq);
    const DishIng = dishIng.DishIngInit(seq);
    const Ingredient = ingredient.IngredientInit(seq, DishIng);
    const Review = review.ReviewInit(seq);
    const Fav = fav.FavInit(seq);
    const Dish = dish.DishInit(seq, RecipeStep, DishIng, Review, Fav);
    const Category = category.CategoryInit(seq, Dish);
    const BuyIng = buyIng.BuyIngredientInit(seq);
    const User = user.UserInit(seq, Dish, BuyIng, Fav, Review);
    const Role = role.RoleInit(seq, User);
    return {
        recipeStepModel: RecipeStep,
        dishIngModel: DishIng,
        dishModel: Dish,
        categoryModel: Category,
        ingredientModel: Ingredient,
        userModel: User,
        roleModel: Role,
        reviewModel: Review,
        buyIngModel: BuyIng,
        favModel: Fav,
    }
}