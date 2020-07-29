const Sequelize = require('sequelize');
const Op = Sequelize.Op;
const sha256 = require('js-sha256');

async function GetAllUsers(Model) {
    try {
        return await Model.findAll({
            attributes: { include: ['RoleId'] }
        });
    }
    catch (err) {
        console.log(err);
    }
}

async function SearchUsers(Model, text) {
    try {
        return await Model.findAll({where:{name: { [Op.like]: text }}});
    }
    catch (err) {
        console.log(err);
    }
}

async function GetUserName(Model, id) {
    try {
        const res = await Model.findAll({where:{id: id}})
        return res[0].name;
    }
    catch (err) {
        console.log(err);
    }
}

async function CheckUser(Model, item) {
    try {
        return await Model.findAll({where:{name: item.name, password: item.password}});
    }
    catch (err) {
        console.log(err);
    }
}

async function GetUser(Model, id) {
    try {
        return await Model.findByPk(id);
    }
    catch (err) {
        console.log(err);
    }
}

async function CreateUser(Model, item) {
    try {
        return await Model.create({
            name: item.name,
            password: item.password,
            date: Date.now(),
            RoleId: item.RoleId,
            email: item.email,
          });
    }
    catch (err) {
        console.log(err);
    }
}

async function UpdateUser(Model, item) {
    await Model.update({ 
        name: item.name,
        password: item.password,
        date: item.date,
        RoleId: item.RoleId,
        email: item.email,
    }, {
        where: {
          id: item.id,
        }
      });
}

async function DeleteUser(Model, id) {
    try {
        return await Model.destroy({
            where: {
              id: id,
            }
          }); 
    }
    catch (err) {
        console.log(err);
    }
}

module.exports.GetAllUsers = GetAllUsers;
module.exports.GetUser = GetUser;
module.exports.CheckUser = CheckUser;
module.exports.GetUserName = GetUserName;
module.exports.CreateUser = CreateUser;
module.exports.UpdateUser = UpdateUser;
module.exports.DeleteUser = DeleteUser;
module.exports.SearchUsers = SearchUsers;