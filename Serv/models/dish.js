const Sequelize = require('sequelize');

module.exports.DishInit = function(sequelize, RecipeStep, DishIng, Review, Fav) {
    const Dish = sequelize.define("Dishes", {
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
      kkal: {
        type: Sequelize.REAL,
        allowNull: true
      },
      rating: {
        type: Sequelize.REAL,
        allowNull: true
      },
      cooktime: {
        type: Sequelize.REAL,
        allowNull: true
      },
      creation_date: {
        type: Sequelize.DATE,
        allowNull: false
      },
      update_date: {
        type: Sequelize.DATE,
        allowNull: false
      },
      is_public: {
        type: Sequelize.BOOLEAN,
        allowNull: false
      }
    });
    Dish.hasMany(RecipeStep, { onDelete: 'cascade', hooks: true });
    Dish.hasMany(DishIng, { onDelete: 'cascade', hooks: true });
    Dish.hasMany(Review, { onDelete: 'cascade', hooks: true });
    Dish.hasMany(Fav, { onDelete: 'cascade', hooks: true });
    return Dish;
}