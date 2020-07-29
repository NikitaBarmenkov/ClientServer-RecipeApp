async function GetAllRoles(Model) {
    try {
        return await Model.findAll();
    }
    catch (err) {
        console.log(err);
    }
}

module.exports.GetAllRoles = GetAllRoles;