package com.quarkus.test

import com.quarkus.test.security.web.AuthenticationResourceTest
import io.quarkus.test.junit.NativeImageTest

@NativeImageTest
open class NativeExampleResourceIT : AuthenticationResourceTest()