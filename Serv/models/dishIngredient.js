const Sequelize = require('sequelize');

module.exports.DishIngInit = function(sequelize) {
    const DishIng = sequelize.define("DishIngredients", {
      id: {
        type: Sequelize.INTEGER,
        autoIncrement: true,
        primaryKey: true,
        allowNull: false
      },
      quantity: {
        type: Sequelize.REAL,
        allowNull: false
      },
      unit: {
        type: Sequelize.STRING,
        allowNull: false
      }
    });
    return DishIng;
}