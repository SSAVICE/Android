package com.ssavice.data.service

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Url

interface ImageUploadService {
    /**
     * S3 Presigned URL을 사용하여 이미지 바이너리 데이터를 직접 업로드합니다.
     *
     * @param url S3에서 발급받은 Presigned URL 전체 주소
     * @param contentType 이미지의 MIME 타입 (예: image/jpeg, image/png)
     * @param body 이미지 바이너리 데이터를 담고 있는 RequestBody
     * @return 성공 시 200 OK 등을 포함한 Response
     */
    @PUT
    suspend fun uploadImage(
        @Url url: String,
        @Header("Content-Type") contentType: String,
        @Body body: RequestBody,
    ): Result<Unit>
}
