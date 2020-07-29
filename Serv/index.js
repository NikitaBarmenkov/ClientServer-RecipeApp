const express = require('express');
const app = express();
const Sequelize = require('sequelize');
const Models = require('./models/ModelsInit');
const bodyParser = require("body-parser");
const CatController = require('./controllers/CatController');
const IngController = require('./controllers/IngController');
const DishController = require('./controllers/DishController');
const UserController = require('./controllers/UserController');
const RoleController = require('./controllers/RoleController');
const FavController = require('./controllers/FavController');
const BuyItemsController = require('./controllers/BuyItemsController');
const ReviewController = require('./controllers/ReviewController');

const sequelize = new Sequelize('CookAppDB', 'nikita', '1234', {
    host: 'localhost',
    dialect: 'postgres',
    define: {
      timestamps: false
    }
  });

app.listen(3030, function () {
  console.log('Example app listening on port 3030!');
});

app.use(bodyParser.json({limit: '50mb'}));
app.use(bodyParser.urlencoded({limit: '50mb', extended: true}));

//all sequelize models
const AllModels = Models.GetModels(sequelize);

//sync models with database
sequelize.sync().then(result => console.log(result))
.catch(err => console.log(err));

CatController.Init(app, AllModels);
IngController.Init(app, AllModels);
DishController.Init(app, AllModels, sequelize);
UserController.Init(app, AllModels);
RoleController.Init(app, AllModels);
FavController.Init(app, AllModels);
BuyItemsController.Init(app, AllModels);
ReviewController.Init(app, AllModels);