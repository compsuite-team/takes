/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.takes.rs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.llorllale.cactoos.matchers.Assertion;

/**
 * Test case for {@link RsPrettyJson}.
 * @since 1.0
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class RsPrettyJsonTest {

    /**
     * RsPrettyJSON can format response with JSON body.
     * @throws Exception If some problem inside
     */
    @Test
    public void formatsJsonBody() throws Exception {
        MatcherAssert.assertThat(
            new RsPrint(
                new RsPrettyJson(
                    new RsWithBody("{\"widget\": {\"debug\": \"on\" }}")
                )
            ).printBody(),
            Matchers.is(
                "\n{\n    \"widget\":{\n        \"debug\":\"on\"\n    }\n}"
            )
        );
    }

    /**
     * RsPrettyJSON can reject a non-JSON body.
     * @throws Exception If some problem inside
     */
    @Test(expected = IOException.class)
    public void rejectsNonJsonBody() throws Exception {
        new RsPrint(new RsPrettyJson(new RsWithBody("foo"))).printBody();
    }

    /**
     * RsPrettyJSON can report correct content length.
     * @throws Exception If some problem inside
     */
    @Test
    public void reportsCorrectContentLength() throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new RsPrint(
            new RsWithBody(
                "\n{\n    \"test\":{\n        \"test\":\"test\"\n    }\n}"
            )
        ).printBody(baos);
        MatcherAssert.assertThat(
            new RsPrint(
                new RsPrettyJson(
                    new RsWithBody("{\"test\": {\"test\": \"test\" }}")
                )
            ).printHead(),
            Matchers.containsString(
                String.format(
                    "Content-Length: %d",
                    baos.toByteArray().length
                )
            )
        );
    }

    /**
     * RsPrettyJSON can conform to equals.
     * @throws Exception If some problem inside
     */
    @Test
    public void mustEvaluateTrueEquality() throws Exception {
        new Assertion<>(
            "Must evaluate true equality",
            new RsPrettyJson(
                new RsWithBody("{\"person\":{\"name\":\"John\"}}")
            ),
            new IsEqual<>(
                new RsPrettyJson(
                    new RsWithBody("{\"person\":{\"name\":\"John\"}}")
                )
            )
        ).affirm();
    }
}
