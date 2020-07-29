const Sequelize = require('sequelize');

module.exports.IngredientInit = function(sequelize, DishIng) {
  const Ingredient = sequelize.define("Ingredients", {
      id: {
        type: Sequelize.INTEGER,
        autoIncrement: true,
        primaryKey: true,
        allowNull: false
      },
      name: {
        type: Sequelize.STRING,
        allowNull: false
      },
    });
    Ingredient.hasMany(DishIng, { onDelete: 'cascade', hooks: true });
    return Ingredient;
}