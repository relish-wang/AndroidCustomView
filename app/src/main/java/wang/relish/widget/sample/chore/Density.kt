/*
 * Copyright (C) guolin, Suzhou Quxiang Inc. Open source codes for study only.
 * Do not use for commercial purpose.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wang.relish.widget.sample.chore

import android.content.Context


/**
 * 单位转换工具类，会根据手机的分辨率来进行单位转换。
 *
 * @author wangxin
 * @since 20191031
 */

/**
 * 根据手机的分辨率将dp转成为px
 */
fun dp2px(context: Context, dp: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率将px转成dp
 */
fun px2dp(context: Context,px: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}
