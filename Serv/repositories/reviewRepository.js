const Sequelize = require('sequelize');
const UserRepository = require('./userRepository');
const DishRepository = require('./dishRepository');

async function GetAllReviews(Model) {
    try {
        return await Model.findAll();
    }
    catch (err) {
        console.log(err);
    }
}

async function GetReviewsByDishId(Models, id) {
    try {
        let res = await Models.reviewModel.findAll({where:{DishId: id}});
        for(i = 0; i < res.length; i++) {
            res[i].userName = await UserRepository.GetUserName(Models.userModel, res[i].UserId);
        }
        return res;
    }
    catch (err) {
        console.log(err);
    }
}

async function GetReviewByUserDishId(Model, item) {
    try {
        return await Model.findAll({where:{DishId: item.DishId, UserId: item.UserId}});
    }
    catch (err) {
        console.log(err);
    }
}

async function CreateReview(Models, item) {
    try {
        const review = await Models.reviewModel.create({
            comment: item.comment,
            rating: item.rating,
            UserId: item.UserId,
            DishId: item.DishId,
            date: Date.now(),
          });
          await HandleDishRatingChange(Models, item.DishId);
          return review;
    }
    catch (err) {
        console.log(err);
    }
}

async function HandleDishRatingChange(Models, DishId) {
    const reviews = await GetReviewsByDishId(Models, DishId);
    try {
        let count = 0, sum = 0;
        reviews.map(val => {
            count++;
            sum = sum + val.rating;
        });
        if (count == 0) {
            obj = {
                DishId: DishId,
                rating: 0,
            }
        }
        else {
            obj = {
                DishId: DishId,
                rating: sum * 1.0/count,
            }
        }
        DishRepository.ChangeRating(Models.dishModel, obj);
    }
    catch(err) {
        console.log(err);
    }
}

async function UpdateReview(Models, item) {
    await Models.reviewModel.update({ 
        comment: item.comment,
        rating: item.rating,
        UserId: item.UserId,
        DishId: item.DishId,
        date: Date.now(),
    }, {
        where: {
            id: item.id
        }
      });
    await HandleDishRatingChange(Models, item.DishId);
}

async function GetReview(Models, id) {
    try {
        return await Models.reviewModel.findAll({where:{id: id}});
        //await HandleDishRatingChange(Models, item.DishId);
    }
    catch (err) {
        console.log(err);
    }
}

async function DeleteReview(Models, id) {
    try {
        await Models.reviewModel.destroy({
            where: {
              id: id,
            }
          });
        await HandleDishRatingChange(Models, item.DishId);
    }
    catch (err) {
        console.log(err);
    }
}

module.exports.GetAllReviews = GetAllReviews;
module.exports.GetReviewByUserDishId = GetReviewByUserDishId;
module.exports.GetReviewsByDishId = GetReviewsByDishId;
module.exports.GetReview = GetReview;
module.exports.CreateReview = CreateReview;
module.exports.UpdateReview = UpdateReview;
module.exports.DeleteReview = DeleteReview;