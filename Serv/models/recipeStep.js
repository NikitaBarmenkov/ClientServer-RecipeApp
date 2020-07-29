const Sequelize = require('sequelize');

module.exports.RecipeStepInit = function(sequelize) {
    const Step = sequelize.define("RecipeSteps", {
      id: {
        type: Sequelize.INTEGER,
        autoIncrement: true,
        primaryKey: true,
        allowNull: false
      },
      text: {
        type: Sequelize.STRING,
        allowNull: false
      },
      number: {
        type: Sequelize.INTEGER,
        allowNull: true
      },
    });
    return Step;
}