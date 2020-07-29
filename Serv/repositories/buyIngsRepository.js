const Sequelize = require('sequelize');

async function GetAllUserBuyIngs(Model, id) {
    try {
        return await Model.findAll({ where: {UserId: id}});
    }
    catch (err) {
        console.log(err);
    }
}

async function GetBuyIng(Model, id) {
    try {
        return await Model.findAll({ where: {id: id}});
    }
    catch (err) {
        console.log(err);
    }
}

async function CreateBuyIng(Model, item) {
    try {
        const existing = await GetAllUserBuyIngs(Model, item.UserId);
        for (i = 0; i < item.items.length; i++) {
            const obj = existing.find(val => val.name == item.items[i].ingredientName);
            if (obj) {//ингредиент есть в списке покупок
                if (obj.unit.includes(item.items[i].unit)) {//единицы измерения совпадают
                    const quantity_arr = obj.quantity.split('/');
                    const unit_arr = obj.unit.split('/');
                    let index = unit_arr.indexOf(item.items[i].unit, 0);
                    if (index != -1) {
                        quantity_arr[index] = quantity_arr[index]*1.0 + item.items[i].quantity;

                        await Model.update({ 
                            quantity: quantity_arr.join('/'),
                            unit: unit_arr.join('/'),
                        }, {
                            where: {
                                id: obj.id
                            }
                        });
                    }
                    else {
                        console.log('index not found');
                    }
                }
                else {//единицы измерения не совпадают
                    obj.unit += '/' + item.items[i].unit;
                    obj.quantity += '/' + item.items[i].quantity;
                    await Model.update({ 
                        quantity: obj.quantity,
                        unit: obj.unit,
                    }, {
                        where: {
                            id: obj.id
                        }
                    });
                }
            }
            else {
                await Model.create({
                    name: item.items[i].ingredientName,
                    quantity: item.items[i].quantity,
                    unit: item.items[i].unit,
                    checked: false,
                    UserId: item.UserId,
                });
            }
        }
    }
    catch (err) {
        console.log(err);
    }
}

async function UpdateBuyIng(Model, item) {
    await Model.update({ 
        checked: item.checked,
    }, {
        where: {
            id: item.id
        }
    });
    return await GetBuyIng(Model, item.id);
}

async function DeleteBuyIng(Model, id) {
    try {
        const allitems = await GetAllUserBuyIngs(Model, id);
        allitems.map(async (item)=>{
            await Model.destroy({
                where: {
                    id: item.id,
                }
                }); 
        });
        return await GetAllUserBuyIngs(Model, id);
    }
    catch (err) {
        console.log(err);
    }
}

module.exports.GetAllUserBuyIngs = GetAllUserBuyIngs;
module.exports.CreateBuyIng = CreateBuyIng;
module.exports.UpdateBuyIng = UpdateBuyIng;
module.exports.DeleteBuyIng = DeleteBuyIng;