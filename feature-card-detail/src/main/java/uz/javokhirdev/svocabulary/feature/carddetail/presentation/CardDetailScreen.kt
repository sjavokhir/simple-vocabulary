package uz.javokhirdev.svocabulary.feature.carddetail.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabFilledButton
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabGradientBackground
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabTextField
import uz.javokhirdev.svocabulary.core.designsystem.component.VocabTopAppBar
import uz.javokhirdev.svocabulary.core.designsystem.icon.VocabIcons
import uz.javokhirdev.svocabulary.core.designsystem.theme.LocalSpacing
import uz.javokhirdev.svocabulary.core.ui.R

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun CardDetailScreen(
    viewModel: CardDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    val uiState = viewModel.uiState

    if (uiState.isSuccess) {
        LaunchedEffect(Unit) { onBackClick() }
    }

    VocabGradientBackground {
        Scaffold(
            topBar = {
                VocabTopAppBar(
                    title = stringResource(id = uiState.resources.toolbarTitle),
                    navigationIcon = VocabIcons.ArrowBack,
                    onNavigationClick = onBackClick,
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                    )
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumedWindowInsets(innerPadding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(spacing.normal)
                ) {
                    VocabTextField(
                        text = uiState.term.orEmpty(),
                        hint = stringResource(id = R.string.enter_term),
                        onValueChange = {
                            viewModel.handleEvent(CardDetailEvent.TermChanged(it))
                        }
                    )
                    Spacer(Modifier.height(spacing.normal))
                    VocabTextField(
                        text = uiState.definition.orEmpty(),
                        hint = stringResource(id = R.string.enter_definition),
                        onValueChange = {
                            viewModel.handleEvent(CardDetailEvent.DefinitionChanged(it))
                        }
                    )
                    Spacer(Modifier.height(spacing.medium))
                    VocabFilledButton(
                        onClick = {
                            viewModel.handleEvent(CardDetailEvent.SaveClick)
                        },
                        text = stringResource(id = uiState.resources.buttonText),
                        leadingIcon = uiState.resources.buttonLeadingIcon,
                        enabled = uiState.isButtonEnabled && !uiState.isLoading
                    )
                }
            }
        }
    }
}
