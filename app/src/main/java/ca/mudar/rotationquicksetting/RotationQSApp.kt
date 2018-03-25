/*
 * Rotation Quick Setting
 * Add a Quick Settings tile to select portrait or landscape screen orientation.
 *
 * Copyright (C) 2017 Mudar Noufal <mn@mudar.ca>
 *
 * This file is part of RotationQS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.mudar.rotationquicksetting

import android.app.Application
import ca.mudar.rotationquicksetting.data.UserPrefs
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class RotationQSApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.USE_CRASHLYTICS) {
            Fabric.with(this, Crashlytics())
        }

        UserPrefs.setDefaults(this)
    }

}
