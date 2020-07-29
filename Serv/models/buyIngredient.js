const Sequelize = require('sequelize');

module.exports.BuyIngredientInit = function(sequelize) {
    const BuyIng = sequelize.define("BuyIngs", {
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
      quantity: {
        type: Sequelize.STRING,
        allowNull: false
      },
      unit: {
        type: Sequelize.STRING,
        allowNull: false
      },
      checked: {
        type: Sequelize.BOOLEAN,
        allowNull: false
      },
    });
    return BuyIng;
}