/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Piasy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.piasy.base.model.provider;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import rx.schedulers.Schedulers;

/**
 * Created by Piasy{github.com/Piasy} on 15/7/24.
 */
@SuppressWarnings(value = { "PMD.NonThreadSafeSingleton", "PMD.DataflowAnomalyAnalysis" })
public final class BriteDbProvider {

    private static volatile BriteDatabase sBriteDb;

    private BriteDbProvider() {
        // singleton
    }

    static BriteDatabase provideBriteDb(final Config config) {
        if (sBriteDb == null) {
            synchronized (BriteDbProvider.class) {
                if (sBriteDb == null) {
                    sBriteDb = SqlBrite.create()
                            .wrapDatabaseHelper(config.sqliteOpenHelper(), Schedulers.io());
                    sBriteDb.setLoggingEnabled(config.enableLogging());
                }
            }
        }
        return sBriteDb;
    }

    @AutoValue
    public abstract static class Config {
        @NonNull
        public static Builder builder() {
            return new AutoValue_BriteDbProvider_Config.Builder();
        }

        public abstract boolean enableLogging();

        public abstract SQLiteOpenHelper sqliteOpenHelper();

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder enableLogging(final boolean enableLogging);

            public abstract Builder sqliteOpenHelper(final SQLiteOpenHelper sqliteOpenHelper);

            public abstract Config build();
        }
    }
}