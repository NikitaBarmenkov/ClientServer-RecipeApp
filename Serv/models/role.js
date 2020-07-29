const Sequelize = require('sequelize');

module.exports.RoleInit = function(sequelize, User) {
  const Role = sequelize.define("Roles", {
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
    Role.hasMany(User);
    return Role;
}