$(document).ready(function () {
    // Initialize datepicker
    $('.datepicker').datepicker({
        format: 'yyyy-mm-dd',
        autoclose: true
    });

    // Function to toggle date inputs state
    function toggleDateInputs(container, isCustom) {
        const $startDate = container.find('.datepicker[id$="StartDate"]');
        const $endDate = container.find('.datepicker[id$="EndDate"]');
        if (isCustom) {
            $startDate.prop('disabled', false).removeClass('disabled');
            $endDate.prop('disabled', false).removeClass('disabled');
        } else {
            $startDate.prop('disabled', true).addClass('disabled');
            $endDate.prop('disabled', true).addClass('disabled');
        }
    }

    // Handle date range type change for each report form
    $('.report-form').each(function () {
        const $form = $(this);
        const $dateRangeInputs = $form.find('input[name="dateRangeType"]');
        $dateRangeInputs.change(function () {
            const isCustom = $(this).val() === 'custom';
            toggleDateInputs($form, isCustom);
        }).trigger('change'); // Trigger on load to set initial state
    });
});