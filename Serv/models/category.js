const Sequelize = require('sequelize');

module.exports.CategoryInit = function(sequelize, Dish) {
  const Category = sequelize.define("Categories", {
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
    Category.hasMany(Dish, { onDelete: 'cascade', hooks: true });
    return Category;
}