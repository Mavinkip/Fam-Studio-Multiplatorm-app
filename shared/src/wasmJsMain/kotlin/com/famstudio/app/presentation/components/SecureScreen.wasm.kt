package com.famstudio.app.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import kotlinx.browser.document
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.events.Event

@Composable
actual fun SecureScreenEffect() {
    DisposableEffect(Unit) {
        val style = document.createElement("style") as HTMLStyleElement
        style.id = "fam-secure-style"
        style.textContent = """
            canvas {
                pointer-events: none !important;
                user-select: none !important;
                -webkit-user-select: none !important;
            }
        """.trimIndent()
        document.head?.appendChild(style)

        val blockContextMenu: (Event) -> Unit = { it.preventDefault() }
        document.addEventListener("contextmenu", blockContextMenu)

        onDispose {
            document.getElementById("fam-secure-style")?.remove()
            document.removeEventListener("contextmenu", blockContextMenu)
        }
    }
}
