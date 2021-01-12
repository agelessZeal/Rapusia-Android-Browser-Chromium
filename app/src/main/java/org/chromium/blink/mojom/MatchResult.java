
// Copyright 2014 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// This file is autogenerated by:
//     mojo/public/tools/bindings/mojom_bindings_generator.py
// For:
//     third_party/blink/public/mojom/cache_storage/cache_storage.mojom
//

package org.chromium.blink.mojom;

import org.chromium.mojo.bindings.DeserializationException;


public final class MatchResult extends org.chromium.mojo.bindings.Union {

    public static final class Tag {
        public static final int Status = 0;
        public static final int Response = 1;
    };
    private int mStatus;
    private FetchApiResponse mResponse;

    public void setStatus(int status) {
        this.mTag = Tag.Status;
        this.mStatus = status;
    }

    public int getStatus() {
        assert this.mTag == Tag.Status;
        return this.mStatus;
    }

    public void setResponse(FetchApiResponse response) {
        this.mTag = Tag.Response;
        this.mResponse = response;
    }

    public FetchApiResponse getResponse() {
        assert this.mTag == Tag.Response;
        return this.mResponse;
    }


    @Override
    protected final void encode(org.chromium.mojo.bindings.Encoder encoder0, int offset) {
        encoder0.encode(org.chromium.mojo.bindings.BindingsHelper.UNION_SIZE, offset);
        encoder0.encode(this.mTag, offset + 4);
        switch (mTag) {
            case Tag.Status: {
                
                encoder0.encode(this.mStatus, offset + 8);
                break;
            }
            case Tag.Response: {
                
                encoder0.encode(this.mResponse, offset + 8, false);
                break;
            }
            default: {
                break;
            }
        }
    }

    public static MatchResult deserialize(org.chromium.mojo.bindings.Message message) {
        return decode(new org.chromium.mojo.bindings.Decoder(message).decoderForSerializedUnion(), 0);
    }

    public static final MatchResult decode(org.chromium.mojo.bindings.Decoder decoder0, int offset) {
        org.chromium.mojo.bindings.DataHeader dataHeader = decoder0.readDataHeaderForUnion(offset);
        if (dataHeader.size == 0) {
            return null;
        }
        MatchResult result = new MatchResult();
        switch (dataHeader.elementsOrVersion) {
            case Tag.Status: {
                
                result.mStatus = decoder0.readInt(offset + org.chromium.mojo.bindings.DataHeader.HEADER_SIZE);
                    CacheStorageError.validate(result.mStatus);
                result.mTag = Tag.Status;
                break;
            }
            case Tag.Response: {
                
                org.chromium.mojo.bindings.Decoder decoder1 = decoder0.readPointer(offset + org.chromium.mojo.bindings.DataHeader.HEADER_SIZE, false);
                result.mResponse = FetchApiResponse.decode(decoder1);
                result.mTag = Tag.Response;
                break;
            }
            default: {
                break;
            }
        }
        return result;
    }
}