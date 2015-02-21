#import "MyViewController.h"

@interface MyViewController ()
@property (nonatomic, strong) UIBarButtonItem *buttonOn;
@property (nonatomic, strong) UIBarButtonItem *buttonOff;

@end

@implementation MyViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonOn = [[UIBarButtonItem alloc] initWithTitle:@"ON" style:UIBarButtonItemStylePlain target:self action:@selector(actionON:)];
    _buttonOff = [[UIBarButtonItem alloc] initWithTitle:@"OFF" style:UIBarButtonItemStylePlain target:self action:@selector(actionOFF:)];
    [_buttonOff setEnabled:NO];
    [self.navigationItem setLeftBarButtonItem:_buttonOn];
    [self.navigationItem setRightBarButtonItem:_buttonOff];
    [self setTitle:@"Network Activity"];
}

-(void)actionON:(id)sender {
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
    [self.buttonOn setEnabled:NO];
    [self.buttonOff setEnabled:YES];
}

-(void)actionOFF:(id)sender {
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
    [self.buttonOn setEnabled:YES];
    [self.buttonOff setEnabled:NO];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
