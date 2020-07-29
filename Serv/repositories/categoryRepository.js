const Sequelize = require('sequelize');
const Op = Sequelize.Op;

async function GetAllCategories(CategoryM) {
    try {
        return await CategoryM.findAll();
    }
    catch (err) {
        console.log(err);
    }
}

async function SearchCategories(CategoryM, text) {
    try {
        return await CategoryM.findAll({where:{name: { [Op.substring]: text }}});
    }
    catch (err) {
        console.log(err);
    }
}

function GetCategory(CategoryM, id) {
    CategoryM.findByPk(id)
    .then(category=>{
        if(!category) return;
        else return category;
    }).catch(err=>console.log(err));
}

async function CreateCategory(CategoryM, item) {
    try {
        return await CategoryM.create({
            name: item.name
          });
    }
    catch (err) {
        console.log(err);
    }
}

function UpdateCategory(CategoryM, item) {
    CategoryM.update({ name: item.name }, {
        where: {
          id: item.id,
        }
      }).then((res) => {
        console.log(res);
      });
}

async function DeleteCategory(CategoryM, id) {
    try {
        return await CategoryM.destroy({
            where: {
              id: id,
            }
          }); 
    }
    catch (err) {
        console.log(err);
    }
}

module.exports.GetAllCategories = GetAllCategories;
module.exports.GetCategory = GetCategory;
module.exports.CreateCategory = CreateCategory;
module.exports.UpdateCategory = UpdateCategory;
module.exports.DeleteCategory = DeleteCategory;
module.exports.SearchCategories = SearchCategories;