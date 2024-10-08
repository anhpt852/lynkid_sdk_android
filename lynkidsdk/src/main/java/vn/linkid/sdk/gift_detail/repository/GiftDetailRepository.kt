package vn.linkid.sdk.gift_detail.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.gift_detail.service.GiftDetailService
import vn.linkid.sdk.models.diamond.GetDiamondMemberInfoResponseModel
import vn.linkid.sdk.models.flash_sale.GetAllFlashSaleProgramResponseModel
import vn.linkid.sdk.models.imedia.TopupRedeemInfo
import vn.linkid.sdk.models.point.Point
import vn.linkid.sdk.models.point.PointResponseModel

class GiftDetailRepository(private val service: GiftDetailService) {

    suspend fun getGiftDetail(id: Int) = service.getGiftDetail(id).map { result ->
        if (result.isSuccess) {
            val giftDetailResponseModel = result.getOrNull()
            if (giftDetailResponseModel != null && giftDetailResponseModel.isSuccess == true) {
                Result.success(giftDetailResponseModel.data!!)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        } else {
            Log.d("GiftDetailRepository", "getGiftDetail: ${result.exceptionOrNull()?.toString()}")
            Result.failure(result.exceptionOrNull()!!)
        }
    }


    suspend fun getPointInfo(): Flow<Result<Point>> =
        service.getPointInfo().map { result ->
            if (result.isSuccess) {
                val pointResponseModel: PointResponseModel? = result.getOrNull()
                if (pointResponseModel != null && pointResponseModel.isSuccess == true) {
                    Result.success(pointResponseModel.data!!.items!!)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "GiftDetailRepository",
                    "getPointInfo: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    suspend fun createTransaction(
        sessionId: String,
        giftCode: String,
        quantity: Int,
        totalAmount: Double,
        description: String,
        topupRedeemInfo: TopupRedeemInfo? = null
    ) = service.createTransaction(sessionId, giftCode, quantity, totalAmount, description, topupRedeemInfo)
        .map { result ->
            if (result.isSuccess) {
                val pairResult = result.getOrNull()
                if (pairResult != null && pairResult.second.isSuccess == true) {
                    Result.success(pairResult.second.data!!)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "GiftDetailRepository",
                    "createTransaction: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    suspend fun confirmTransaction(
        sessionId: String,
        otpCode: String
    ) = service.confirmTransaction(sessionId, otpCode).map { result ->
        if (result.isSuccess) {
            val pairResult = result.getOrNull()
            if (pairResult != null && pairResult.second.isSuccess == true) {
                Result.success(pairResult.second.data!!)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        } else {
            Log.d(
                "GiftDetailRepository",
                "createTransaction: ${result.exceptionOrNull()?.toString()}"
            )
            Result.failure(result.exceptionOrNull()!!)
        }
    }

    suspend fun getAllFlashSaleProgram(): Flow<Result<GetAllFlashSaleProgramResponseModel>> =
        service.getAllFlashSaleProgram().map { result ->
            if (result.isSuccess) {
                val getAllFlashSaleProgramResponseModel: GetAllFlashSaleProgramResponseModel? =
                    result.getOrNull()
                if (getAllFlashSaleProgramResponseModel != null) {
                    Result.success(getAllFlashSaleProgramResponseModel)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "GiftDetailRepository",
                    "getAllFlashSaleProgram: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }


    suspend fun isUserDiamond(): Flow<Result<Boolean>> =
        service.getDiamondMemberInfo().map { result ->
            if (result.isSuccess) {
                val getDiamondMemberInfoResponseModel: GetDiamondMemberInfoResponseModel? =
                    result.getOrNull()
                if (getDiamondMemberInfoResponseModel != null && getDiamondMemberInfoResponseModel.success == true) {
                    Result.success(getDiamondMemberInfoResponseModel.memberInfor?.segment.equals("af"))
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "GiftDetailRepository",
                    "isUserDiamond: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }
}