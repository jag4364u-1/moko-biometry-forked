package dev.icerock.moko.biometry

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BiometryAuthenticatorTest {

    private val context: Context = mockk(relaxed = true)
    private val biometricManager: BiometricManager = mockk()
    private lateinit var authenticator: BiometryAuthenticator

    @BeforeEach
    fun setUp() {
        mockkStatic(BiometricManager::class)
        every { BiometricManager.from(context) } returns biometricManager
        authenticator = BiometryAuthenticator(context)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(BiometricManager::class)
    }

    @Test
    fun `isBiometricAvailable returns true when biometric authentication is available`() {
        every { biometricManager.canAuthenticate(BIOMETRIC_WEAK) } returns BiometricManager.BIOMETRIC_SUCCESS

        assertTrue(authenticator.isBiometricAvailable())
    }

    @Test
    fun `isBiometricAvailable returns false when no hardware detected`() {
        every { biometricManager.canAuthenticate(BIOMETRIC_WEAK) } returns BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE

        assertFalse(authenticator.isBiometricAvailable())
    }

    @Test
    fun `isBiometricAvailable returns false when hardware unavailable`() {
        every { biometricManager.canAuthenticate(BIOMETRIC_WEAK) } returns BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE

        assertFalse(authenticator.isBiometricAvailable())
    }

    @Test
    fun `isBiometricAvailable returns false when no biometrics enrolled`() {
        every { biometricManager.canAuthenticate(BIOMETRIC_WEAK) } returns BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED

        assertFalse(authenticator.isBiometricAvailable())
    }

    @Test
    fun `isBiometricAvailable returns false when security update required`() {
        every { biometricManager.canAuthenticate(BIOMETRIC_WEAK) } returns BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED

        assertFalse(authenticator.isBiometricAvailable())
    }

    @Test
    fun `isBiometricAvailable returns false for unknown status code`() {
        every { biometricManager.canAuthenticate(BIOMETRIC_WEAK) } returns BiometricManager.BIOMETRIC_STATUS_UNKNOWN

        assertFalse(authenticator.isBiometricAvailable())
    }
}
