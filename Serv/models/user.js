const Sequelize = require('sequelize');

module.exports.UserInit = function(sequelize, Dish, BuyIng, Fav, Review) {
  const User = sequelize.define("Users", {
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
      password: {
        type: Sequelize.STRING,
        allowNull: false
      },
      email: {
        type: Sequelize.STRING,
        allowNull: true
      }
  });
  User.hasMany(Dish, { onDelete: 'cascade', hooks: true });
  User.hasMany(BuyIng, { onDelete: 'cascade', hooks: true });
  User.hasMany(Fav, { onDelete: 'cascade', hooks: true });
  User.hasMany(Review, { onDelete: 'cascade', hooks: true });
  return User;
}