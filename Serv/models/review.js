const Sequelize = require('sequelize');

module.exports.ReviewInit = function(sequelize) {
    const Review = sequelize.define("Reviews", {
      id: {
        type: Sequelize.INTEGER,
        autoIncrement: true,
        primaryKey: true,
        allowNull: false
      },
      comment: {
        type: Sequelize.STRING,
        allowNull: true
      },
      rating: {
        type: Sequelize.REAL,
        allowNull: false
      },
      date: {
        type: Sequelize.DATE,
        allowNull: false
      },
    });
    return Review;
}