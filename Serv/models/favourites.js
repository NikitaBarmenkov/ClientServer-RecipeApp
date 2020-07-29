const Sequelize = require('sequelize');

module.exports.FavInit = function(sequelize) {
    const Fav = sequelize.define("Favourites", {
      id: {
        type: Sequelize.INTEGER,
        autoIncrement: true,
        primaryKey: true,
        allowNull: false
      },
    });
    return Fav;
}