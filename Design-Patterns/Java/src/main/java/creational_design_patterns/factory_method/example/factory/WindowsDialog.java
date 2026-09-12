package creational_design_patterns.factory_method.example.factory;

import creational_design_patterns.factory_method.example.buttons.Button;
import creational_design_patterns.factory_method.example.buttons.WindowsButton;

/**
 * EN: Windows Dialog will produce Windows buttons.
 *
 * <p>RU: Диалог на элементах операционной системы.
 */
public class WindowsDialog extends Dialog {

    @Override
    public Button createButton() {
        return new WindowsButton();
    }
}
